DESCRIPTION = "STM32MP Bootloader python signing tools"
LICENSE = "GPLv3"
LIC_FILES_CHKSUM = "file://LICENSE;md5=1ebbd3e34237af26da5dc08a4e440464"

SRCREV = "1ee6ca5afdf3115cb51149971f0b12431a2a5974"
SRC_URI = "git://github.com/mrnuke/stm32mp-keygen.git;protocol=https;branch=master"

S = "${WORKDIR}/git"

RDEPENDS:${PN} += "python3-core python3-pycryptodomex"

do_install () {

	install -d ${D}${bindir}
	install -m 0755 ${S}/stm32-sign.py   ${D}${bindir}/stm32-sign
	install -m 0755 ${S}/ecdsa-sha256.py ${D}${bindir}/ecdsa-sha256
}

do_install:append:class-native() {

	# use yocto native python instead of host python
	sed -i 's|/usr/bin/env python3|'"${RECIPE_SYSROOT_NATIVE}/${bindir_native}"'/python3-native/python3|g' ${D}${bindir}/stm32-sign
	sed -i 's|/usr/bin/env python3|'"${RECIPE_SYSROOT_NATIVE}/${bindir_native}"'/python3-native/python3|g' ${D}${bindir}/ecdsa-sha256
}

FILES:${PN} = "${bindir}"

BBCLASSEXTEND = "native nativesdk"
