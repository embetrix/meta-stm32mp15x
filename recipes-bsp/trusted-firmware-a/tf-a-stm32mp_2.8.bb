SUMMARY = "Trusted Firmware-A for STM32MP1"
SECTION = "bootloaders"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://license.rst;md5=1dd070c98a281d18d9eefd938729b031"

inherit deploy stm32mp15x-sign

PROVIDES += "virtual/trusted-firmware-a"

SRC_URI = "git://github.com/STMicroelectronics/arm-trusted-firmware;protocol=https;branch=${SRCBRANCH}"
SRCBRANCH = "v2.8-stm32mp"
SRCREV = "61924c04caa485af6d4be4663b4977f6ac226ca0"

# Add MBEDTLS support required for TRUSTED_BOARD_BOOT
MBEDTLS_DIR = "${WORKDIR}/mbedtls"
# MBEDTLS v2.28.0
SRC_URI += "git://github.com/ARMmbed/mbedtls.git;protocol=https;destsuffix=${MBEDTLS_DIR};nobranch=1;name=mbedtls"
SRCREV_mbedtls = "8b3f26a5ac38d4fdccbc5c5366229f3e01dafcc0"
LIC_FILES_CHKSUM += "file://${MBEDTLS_DIR}/LICENSE;md5=3b83ef96387f14655fc854ddc3c6bd57"
LICENSE_MBEDTLS = "Apache-2.0"

DEPENDS += "dtc-native openssl-native util-linux-native tf-a-stm32mp-tools-native stm32mp-keygen-native"

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
EXTRA_OEMAKE += "STM32MP1_OPTEE_IN_SYSRAM=1"
EXTRA_OEMAKE += "DTB_FILE_NAME=${TFA_DEVICETREE}.${DT_SUFFIX}"

# FIP signing configuration
EXTRA_OEMAKE += "TRUSTED_BOARD_BOOT=1"
EXTRA_OEMAKE += "MBEDTLS_DIR=${MBEDTLS_DIR}"

do_compile:prepend() {

    unset LDFLAGS
    unset CFLAGS
    unset CPPFLAGS
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


FILES:${PN} = "/boot"

COMPATIBLE_MACHINE = "(stm32mp15x)"
