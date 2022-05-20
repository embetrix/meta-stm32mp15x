SUMMARY = "Trusted Firmware-A for STM32MP1"
SECTION = "bootloaders"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://license.rst;md5=1dd070c98a281d18d9eefd938729b031"

inherit deploy

PROVIDES += "virtual/trusted-firmware-a"

SRCBRANCH = "v2.2-stm32mp"
SRCREV = "a4c92eca15fab3d985bfd93f2bbdb5dc84298729"

SRC_URI = "git://github.com/STMicroelectronics/arm-trusted-firmware;protocol=https;branch=${SRCBRANCH}"
SRC_URI += "file://0001-correct-DTC-version-detection.patch"

DEPENDS += "dtc-native"

PACKAGE_ARCH = "${MACHINE_ARCH}"

S = "${WORKDIR}/git"

# Define default TF-A namings
TF_A_BASENAME = "tf-a"
TF_A_SUFFIX = "stm32"
DT_SUFFIX = "dtb"

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

do_install() {
                                          
    install -d ${D}/boot
    install -m 644 ${S}/build/stm32mp1/release/${TF_A_BASENAME}-${TFA_DEVICETREE}.${TF_A_SUFFIX} ${D}/boot
}

do_deploy() {
                                          
    install -d ${DEPLOYDIR}
    install -m 644 ${S}/build/stm32mp1/release/${TF_A_BASENAME}-${TFA_DEVICETREE}.${TF_A_SUFFIX} ${DEPLOYDIR}/
    cd ${DEPLOYDIR}/
    ln -sf  ${TF_A_BASENAME}-${TFA_DEVICETREE}.${TF_A_SUFFIX} ${TF_A_BASENAME}.${TF_A_SUFFIX}
}

addtask deploy before do_package after do_compile

FILES:${PN} = "/boot"

COMPATIBLE_MACHINE = "(stm32mp15x)"
