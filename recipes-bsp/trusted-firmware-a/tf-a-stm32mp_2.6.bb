SUMMARY = "Trusted Firmware-A for STM32MP1"
SECTION = "bootloaders"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://license.rst;md5=1dd070c98a281d18d9eefd938729b031"

inherit deploy

PROVIDES += "virtual/trusted-firmware-a"

SRCBRANCH = "v2.6-stm32mp"
SRCREV = "4b6e8e9bf8fa676ff8d1358ea2cf2e44904c2473"

SRC_URI = "git://github.com/STMicroelectronics/arm-trusted-firmware;protocol=https;branch=${SRCBRANCH}"

DEPENDS += "dtc-native"

do_compile[depends] += " \
    virtual/bootloader:do_deploy \
    ${@bb.utils.contains('MACHINE_FEATURES', 'optee', 'virtual/optee-os:do_deploy', '', d)} \
"

PACKAGE_ARCH = "${MACHINE_ARCH}"

S = "${WORKDIR}/git"

# Define default TF-A namings
TF_A_BASENAME = "tf-a"
TF_A_SUFFIX = "stm32"
DT_SUFFIX = "dtb"
FIP_BASENAME="fip"

# Configure build type: debug/release
TFA_BUILD_TYPE ?= "release"

# Configure stm32mp1 make settings
EXTRA_OEMAKE  = 'CROSS_COMPILE=${TARGET_PREFIX}'
EXTRA_OEMAKE += "PLAT=stm32mp1"
EXTRA_OEMAKE += "ARCH=aarch32"
EXTRA_OEMAKE += "ARM_ARCH_MAJOR=7"
EXTRA_OEMAKE += "AARCH32_SP=${@bb.utils.contains('MACHINE_FEATURES', 'optee', 'optee', 'sp_min', d)}"
EXTRA_OEMAKE += "${@bb.utils.contains('TFA_BUILD_TYPE', 'release', '', 'DEBUG=1 LOG_LEVEL=40', d)}"
EXTRA_OEMAKE += "STM32MP_UART_PROGRAMMER=0"
EXTRA_OEMAKE += "STM32MP_USB_PROGRAMMER=${@bb.utils.contains('TFA_BUILD_TYPE', 'release', '1', '0', d)}"
EXTRA_OEMAKE += "STM32MP_SPI_NAND=0"
EXTRA_OEMAKE += "STM32MP_RAW_NAND=0"
EXTRA_OEMAKE += "STM32MP_SPI_NOR=0"
EXTRA_OEMAKE += "STM32MP_EMMC=0"
EXTRA_OEMAKE += "STM32MP_SDMMC=1"
EXTRA_OEMAKE += "DTB_FILE_NAME=${TFA_DEVICETREE}.${DT_SUFFIX}"

do_compile:prepend() {

    unset LDFLAGS
    unset CFLAGS
    unset CPPFLAGS
}

# Generate FIP
do_compile:append() {

    if ${@bb.utils.contains('MACHINE_FEATURES', 'optee', 'true', 'false', d)}; then
        oe_runmake fip 	\
            BL32=${DEPLOY_DIR_IMAGE}/optee/${OPTEE_HEADER}-${OPTEE_CONF}.${OPTEE_SUFFIX} \
            BL32_EXTRA1=${DEPLOY_DIR_IMAGE}/optee/${OPTEE_PAGER}-${OPTEE_CONF}.${OPTEE_SUFFIX} \
            BL32_EXTRA2=${DEPLOY_DIR_IMAGE}/optee/${OPTEE_PAGEABLE}-${OPTEE_CONF}.${OPTEE_SUFFIX} \
            FW_CONFIG=${S}/build/stm32mp1/${TFA_BUILD_TYPE}/fw-config.dtb \
            BL33=${DEPLOY_DIR_IMAGE}/u-boot-nodtb-${MACHINE}.bin \
            BL33_CFG=${DEPLOY_DIR_IMAGE}/u-boot-${MACHINE}.dtb
    else
        oe_runmake fip 	\
            FW_CONFIG=${S}/build/stm32mp1/${TFA_BUILD_TYPE}/fw-config.dtb \
            BL33=${DEPLOY_DIR_IMAGE}/u-boot-nodtb-${MACHINE}.bin \
            BL33_CFG=${DEPLOY_DIR_IMAGE}/u-boot-${MACHINE}.dtb
    fi
}

do_install() {
                                          
    install -d ${D}/boot
    install -m 644 ${S}/build/stm32mp1/${TFA_BUILD_TYPE}/${TF_A_BASENAME}-${TFA_DEVICETREE}.${TF_A_SUFFIX} ${D}/boot
}

do_deploy() {
                                          
    install -d ${DEPLOYDIR}
    install -m 644 ${S}/build/stm32mp1/${TFA_BUILD_TYPE}/${TF_A_BASENAME}-${TFA_DEVICETREE}.${TF_A_SUFFIX} ${DEPLOYDIR}/
    install -m 644 ${S}/build/stm32mp1/${TFA_BUILD_TYPE}/${FIP_BASENAME}.bin ${DEPLOYDIR}/${FIP_BASENAME}-${MACHINE}.bin
    cd ${DEPLOYDIR}/
    ln -sf  ${TF_A_BASENAME}-${TFA_DEVICETREE}.${TF_A_SUFFIX} ${TF_A_BASENAME}.${TF_A_SUFFIX}
    ln -sf  ${FIP_BASENAME}-${MACHINE}.bin ${FIP_BASENAME}.bin
}

addtask deploy before do_package after do_compile

FILES:${PN} = "/boot"

COMPATIBLE_MACHINE = "(stm32mp15x)"
