SUMMARY = "Trusted Firmware-A Tools"
SECTION = "bootloaders"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://license.rst;md5=1dd070c98a281d18d9eefd938729b031"

SRC_URI = "git://github.com/STMicroelectronics/arm-trusted-firmware;protocol=https;branch=${SRCBRANCH}"
SRCBRANCH = "v2.8-stm32mp"
SRCREV = "648de4789e0456eb9f874d6dd2151baaa85032d3"

DEPENDS += "dtc-native openssl-native"

S = "${WORKDIR}/git"

EXTRA_OEMAKE += "PLAT=stm32mp1"

do_compile:prepend() {

    sed -i '/^LIB_DIR/ s,$, \$\{BUILD_LDFLAGS},'      ${S}/tools/cert_create/Makefile
    sed -i '/^INC_DIR/ s,$, \$\{BUILD_CFLAGS},'       ${S}/tools/cert_create/Makefile

    sed -i '/^LDLIBS/ s,$, \$\{BUILD_LDFLAGS},'       ${S}/tools/fiptool/Makefile
    sed -i '/^INCLUDE_PATHS/ s,$, \$\{BUILD_CFLAGS},' ${S}/tools/fiptool/Makefile
}
