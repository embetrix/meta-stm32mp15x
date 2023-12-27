require recipes-bsp/u-boot/u-boot-common.inc
require recipes-bsp/u-boot/u-boot.inc
LIC_FILES_CHKSUM = "file://Licenses/README;md5=2ca5f2c35c8cc335f0a19756634782f1"

FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

DEPENDS += "bc-native dtc-native u-boot-tools-native"
DEPENDS += "python3-setuptools-native"

SRCBRANCH = "v2022.10-stm32mp"
SRCREV = "2c7a6accfa78d34c402fa262bb53f0b952198882"

SRC_URI  = "git://github.com/STMicroelectronics/u-boot.git;protocol=https;branch=${SRCBRANCH}"
SRC_URI += "file://0001-add-signature-nodes-to-dts-files.patch
SRC_URI += "file://0002-shift-kernel-load-address.patch
SRC_URI += "file://0003-rework-CONFIG_EXTRA_ENV_SETTINGS.patch
SRC_URI += "file://0004-rework-stm32mp15x-defconfig.patch
SRC_URI += "file://template.its"
SRC_URI += "file://embetrix.png"

UBOOT_INITIAL_ENV = "u-boot-initial-env"

COMPATIBLE_MACHINE = "(stm32mp15x)"

do_add_logo[depends] += "imagemagick-native:do_populate_sysroot"
addtask add_logo after do_patch before do_compile

do_add_logo () {

    convert.im7 -rotate 270 -depth 8 -colors 256 -compress none \
                ${WORKDIR}/embetrix.png \
                BMP3:${S}/tools/logos/st.bmp
}        

do_compile:prepend () {

    unset LDFLAGS
    unset CFLAGS
  
    # In case of Verified boot we will sign a dummy fitImage and append the public key to U-Boot dtbs. 
    # We ensure that the U-Boot dtbs are deployed with public key before generating final u-boot image
    if [ "${UBOOT_SIGN_ENABLE}" = "1" ] ; then

        # Generate dummy fitImage with corresponding key-hint-name and hash/sign algos from Bitbake variables
        sed -i "s/__KEY_NAME_HINT__/${UBOOT_SIGN_KEYNAME}/g" ${WORKDIR}/template.its
        sed -i "s/__FIT_HASH_ALG__/${FIT_HASH_ALG}/g"        ${WORKDIR}/template.its
        sed -i "s/__FIT_SIGN_ALG__/${FIT_SIGN_ALG}/g"        ${WORKDIR}/template.its

        touch ${WORKDIR}/dummy-kernel.bin ${WORKDIR}/dummy-kernel.dtb

        # Build Device Tree blobs
        oe_runmake -C ${S} O=${B} ${UBOOT_MAKE_TARGET} dtbs
        
        bbwarn "uboot-mkimage \
            ${@'-D "${UBOOT_MKIMAGE_DTCOPTS}"' if len('${UBOOT_MKIMAGE_DTCOPTS}') else ''} \
            ${UBOOT_MKIMAGE_SIGN_ARGS} \
            -F -k '${UBOOT_SIGN_KEYDIR}' \
            -K  ${B}/arch/arm/dts/${UBOOT_DEVICETREE}.dtb \
            -r -f ${WORKDIR}/template.its ${B}/template.fit"
        uboot-mkimage \
            ${@'-D "${UBOOT_MKIMAGE_DTCOPTS}"' if len('${UBOOT_MKIMAGE_DTCOPTS}') else ''} \
            ${UBOOT_MKIMAGE_SIGN_ARGS} \
            -F -k '${UBOOT_SIGN_KEYDIR}' \
            -K  ${B}/arch/arm/dts/${UBOOT_DEVICETREE}.dtb \
            -r -f ${WORKDIR}/template.its ${B}/template.fit
    fi     
}

do_deploy:append() {

    install -m 644 ${B}/u-boot-nodtb.bin ${DEPLOYDIR}/u-boot-nodtb-${MACHINE}.bin
    install -m 644 ${B}/u-boot.dtb ${DEPLOYDIR}/u-boot-${MACHINE}.dtb
    cd ${DEPLOYDIR}
    ln -snf  u-boot-nodtb-${MACHINE}.bin u-boot-nodtb.bin
    ln -snf  u-boot-${MACHINE}.dtb u-boot.dtb
    cd -
}
