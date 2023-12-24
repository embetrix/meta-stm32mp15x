SUMMARY = "Trusted Firmware-A for STM32MP1"
SECTION = "bootloaders"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://license.rst;md5=1dd070c98a281d18d9eefd938729b031"

inherit deploy

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

do_tfa_sign() {

    stm32-sign  -k "${SECBOOT_SIGN_KEY}" \
                -s ${B}/build/stm32mp1/${TFA_BUILD_TYPE}/${TF_A_BASENAME}-${TFA_DEVICETREE}.${TF_A_SUFFIX} \
                -o ${B}/build/stm32mp1/${TFA_BUILD_TYPE}/${TF_A_BASENAME}-${TFA_DEVICETREE}.${TF_A_SUFFIX}.sign

}

do_fip_sign() {

    install -d  ${B}/build/stm32mp1/${TFA_BUILD_TYPE}/certs
    cert_create --rot-key "${SECBOOT_SIGN_KEY}" -n                                                              \
                --tfw-nvctr          0                                                                          \
                --ntfw-nvctr         0                                                                          \
                --key-alg            ecdsa                                                                      \
                --hash-alg           sha256                                                                     \
                --tos-fw             ${DEPLOY_DIR_IMAGE}/optee/${OPTEE_HEADER}-${OPTEE_CONF}.${OPTEE_SUFFIX}    \
                --tos-fw-extra1      ${DEPLOY_DIR_IMAGE}/optee/${OPTEE_PAGER}-${OPTEE_CONF}.${OPTEE_SUFFIX}     \
                --tos-fw-extra2      ${DEPLOY_DIR_IMAGE}/optee/${OPTEE_PAGEABLE}-${OPTEE_CONF}.${OPTEE_SUFFIX}  \
                --nt-fw              ${DEPLOY_DIR_IMAGE}/u-boot-nodtb-${MACHINE}.bin                            \
                --hw-config          ${DEPLOY_DIR_IMAGE}/u-boot-${MACHINE}.dtb                                  \
                --fw-config          ${B}/build/stm32mp1/${TFA_BUILD_TYPE}/fdts/${TFA_DEVICETREE}-fw-config.dtb \
                --tos-fw-cert        ${B}/build/stm32mp1/${TFA_BUILD_TYPE}/certs/tee-header_v2.crt              \
                --trusted-key-cert   ${B}/build/stm32mp1/${TFA_BUILD_TYPE}/certs/trusted-key-cert.key-crt       \
                --tos-fw-key-cert    ${B}/build/stm32mp1/${TFA_BUILD_TYPE}/certs/tee-header_v2.key-crt          \
                --nt-fw-cert         ${B}/build/stm32mp1/${TFA_BUILD_TYPE}/certs/u-boot.crt                     \
                --nt-fw-key-cert     ${B}/build/stm32mp1/${TFA_BUILD_TYPE}/certs/u-boot.key-crt                 \
                --stm32mp-cfg-cert   ${B}/build/stm32mp1/${TFA_BUILD_TYPE}/certs/stm32mp_cfg_cert.crt

    fiptool create ${B}/build/stm32mp1/${TFA_BUILD_TYPE}/${FIP_BASENAME}.bin.sign                               \
                --tos-fw             ${DEPLOY_DIR_IMAGE}/optee/${OPTEE_HEADER}-${OPTEE_CONF}.${OPTEE_SUFFIX}    \
                --tos-fw-extra1      ${DEPLOY_DIR_IMAGE}/optee/${OPTEE_PAGER}-${OPTEE_CONF}.${OPTEE_SUFFIX}     \
                --tos-fw-extra2      ${DEPLOY_DIR_IMAGE}/optee/${OPTEE_PAGEABLE}-${OPTEE_CONF}.${OPTEE_SUFFIX}  \
                --nt-fw              ${DEPLOY_DIR_IMAGE}/u-boot-nodtb-${MACHINE}.bin                            \
                --hw-config          ${DEPLOY_DIR_IMAGE}/u-boot-${MACHINE}.dtb                                  \
                --fw-config          ${B}/build/stm32mp1/${TFA_BUILD_TYPE}/fdts/${TFA_DEVICETREE}-fw-config.dtb \
                --tos-fw-cert        ${B}/build/stm32mp1/${TFA_BUILD_TYPE}/certs/tee-header_v2.crt              \
                --trusted-key-cert   ${B}/build/stm32mp1/${TFA_BUILD_TYPE}/certs/trusted-key-cert.key-crt       \
                --tos-fw-key-cert    ${B}/build/stm32mp1/${TFA_BUILD_TYPE}/certs/tee-header_v2.key-crt          \
                --nt-fw-cert         ${B}/build/stm32mp1/${TFA_BUILD_TYPE}/certs/u-boot.crt                     \
                --nt-fw-key-cert     ${B}/build/stm32mp1/${TFA_BUILD_TYPE}/certs/u-boot.key-crt                 \
                --stm32mp-cfg-cert   ${B}/build/stm32mp1/${TFA_BUILD_TYPE}/certs/stm32mp_cfg_cert.crt           
}

do_deploy() {
                                          
    install -d ${DEPLOYDIR}
    install -m 644 ${S}/build/stm32mp1/${TFA_BUILD_TYPE}/${TF_A_BASENAME}-${TFA_DEVICETREE}.${TF_A_SUFFIX}.sign ${DEPLOYDIR}/
    install -m 644 ${S}/build/stm32mp1/${TFA_BUILD_TYPE}/${FIP_BASENAME}.bin.sign ${DEPLOYDIR}/${FIP_BASENAME}-${MACHINE}.bin.sign
    cd ${DEPLOYDIR}/
    ln -sf  ${TF_A_BASENAME}-${TFA_DEVICETREE}.${TF_A_SUFFIX}.sign ${TF_A_BASENAME}.${TF_A_SUFFIX}
    ln -sf  ${FIP_BASENAME}-${MACHINE}.bin.sign ${FIP_BASENAME}.bin
}

do_deploy:append() {

    openssl ec -in ${SECBOOT_SIGN_KEY}  -outform PEM -out ${DEPLOYDIR}/secureboot-pubkey.pem -pubout
    ecdsa-sha256 --public-key=${DEPLOYDIR}/secureboot-pubkey.pem \
                 --binhash-file=${DEPLOYDIR}/secureboot-pubhash.bin
    
    # Generate u-boot cmd to fuse public key hashes into OTP
    echo fuse prog -y 0 0x18 $(hexdump -e '/4 "0x"' -e '/1 "%x"' -e '" "' ${DEPLOYDIR}/secureboot-pubhash.bin) > ${DEPLOYDIR}/u-boot-fuse-prog.txt
}

addtask do_tfa_sign after do_compile
addtask do_fip_sign after do_compile
addtask deploy before do_package after do_fip_sign
addtask deploy before do_package after do_tfa_sign

FILES:${PN} = "/boot"

COMPATIBLE_MACHINE = "(stm32mp15x)"
