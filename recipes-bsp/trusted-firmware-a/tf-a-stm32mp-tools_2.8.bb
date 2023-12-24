SUMMARY = "Trusted Firmware-A Tools"
SECTION = "tools"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://license.rst;md5=1dd070c98a281d18d9eefd938729b031"

SRC_URI = "git://github.com/STMicroelectronics/arm-trusted-firmware;protocol=https;branch=${SRCBRANCH}"
SRCBRANCH = "v2.8-stm32mp"
SRCREV = "61924c04caa485af6d4be4663b4977f6ac226ca0"

DEPENDS += "openssl"

S = "${WORKDIR}/git"

EXTRA_OEMAKE += "PLAT=stm32mp1"

do_compile:prepend() {

    sed -i '/^INCLUDE_PATHS/ s,$, \${BUILD_CFLAGS},' ${S}/tools/fiptool/Makefile
    sed -i '/^OPENSSL_DIR/c\\OPENSSL_DIR := \${RECIPE_SYSROOT_NATIVE}/usr' ${S}/tools/fiptool/Makefile
    sed -i '/^LDLIBS/ s,$,  \-Wl\,\-R\${RECIPE_SYSROOT_NATIVE}/usr/lib,' ${S}/tools/fiptool/Makefile
}

do_compile() {
                                          
    oe_runmake certtool fiptool enctool
}

do_install() {
                                          
	install -d ${D}${bindir}

	install -m 0755 ${B}/tools/fiptool/fiptool         ${D}${bindir}
	install -m 0755 ${B}/tools/cert_create/cert_create ${D}${bindir}
	install -m 0755 ${B}/tools/encrypt_fw/encrypt_fw   ${D}${bindir}
}


FILES:${PN} = "${bindir}"

BBCLASSEXTEND = "native nativesdk"
