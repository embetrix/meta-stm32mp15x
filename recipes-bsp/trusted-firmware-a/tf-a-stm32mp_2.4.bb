SUMMARY = "Trusted Firmware-A for STM32MP1"
SECTION = "bootloaders"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://license.rst;md5=1dd070c98a281d18d9eefd938729b031"

inherit deploy

PROVIDES += "virtual/trusted-firmware-a"

SRCBRANCH = "v2.4-stm32mp"
SRCREV = "74e6754b9f7086fdd4545cbc2f606799c09f5cbb"

SRC_URI = "git://github.com/STMicroelectronics/arm-trusted-firmware;protocol=https;branch=${SRCBRANCH}"
SRC_URI += "file://0001-correct-DTC-version-detection.patch"

DEPENDS += "dtc-native"
do_compile[depends] += "virtual/bootloader:do_deploy"

PACKAGE_ARCH = "${MACHINE_ARCH}"

S = "${WORKDIR}/git"

# Define default TF-A namings
TF_A_BASENAME = "tf-a"
TF_A_SUFFIX = "stm32"
DT_SUFFIX = "dtb"
FIP_BASENAME="fip"

# Configure stm32mp1 make settings
EXTRA_OEMAKE  = 'CROSS_COMPILE=${TARGET_PREFIX}'
EXTRA_OEMAKE += "PLAT=stm32mp1"
EXTRA_OEMAKE += "ARCH=aarch32"
EXTRA_OEMAKE += "ARM_ARCH_MAJOR=7"
EXTRA_OEMAKE += "AARCH32_SP=sp_min"
EXTRA_OEMAKE += "STM32MP_UART_PROGRAMMER=0"
EXTRA_OEMAKE += "STM32MP_USB_PROGRAMMER=1"
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
                                          
    oe_runmake fip 	\
        BL32=${S}/build/stm32mp1/release/bl32.bin \
        FW_CONFIG=${S}/build/stm32mp1/release/fw-config.dtb \
        BL33=${DEPLOY_DIR_IMAGE}/u-boot-nodtb-${MACHINE}.bin \
        BL33_CFG=${DEPLOY_DIR_IMAGE}/u-boot-${MACHINE}.dtb
}

do_install() {
                                          
    install -d ${D}/boot
    install -m 644 ${S}/build/stm32mp1/release/${TF_A_BASENAME}-${TFA_DEVICETREE}.${TF_A_SUFFIX} ${D}/boot
}

do_deploy() {
                                          
    install -d ${DEPLOYDIR}
    install -m 644 ${S}/build/stm32mp1/release/${TF_A_BASENAME}-${TFA_DEVICETREE}.${TF_A_SUFFIX} ${DEPLOYDIR}/
    install -m 644 ${S}/build/stm32mp1/release/${FIP_BASENAME}.bin ${DEPLOYDIR}/${FIP_BASENAME}-${MACHINE}.bin
    cd ${DEPLOYDIR}/
    ln -sf  ${TF_A_BASENAME}-${TFA_DEVICETREE}.${TF_A_SUFFIX} ${TF_A_BASENAME}.${TF_A_SUFFIX}
    ln -sf  ${FIP_BASENAME}-${MACHINE}.bin ${FIP_BASENAME}.bin
}

addtask deploy before do_package after do_compile

FILES:${PN} = "/boot"

COMPATIBLE_MACHINE = "(stm32mp15x)"
