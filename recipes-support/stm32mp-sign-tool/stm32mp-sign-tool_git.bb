SUMMARY = "STM32MP Sign Tool is a utility"
DESCRIPTION = "The STM32MP Sign Tool is a utility for \
signing and verifying firmware images for STM32MP MPUs. \
It uses ECDSA (Elliptic Curve Digital Signature Algorithm) to ensure the integrity and authenticity of the firmware."
HOMEPAGE = "https://github.com/embetrix/stm32mp-sign-tool"
SECTION = "console/utils"
LICENSE = "GPL-3.0-only"
LIC_FILES_CHKSUM = "file://LICENSE;md5=e49f4652534af377a713df3d9dec60cb"

SRC_URI = "git://github.com/embetrix/${BPN};branch=master;protocol=https"
SRCREV = "3166bda08ea2d1647d744ca53c8fd4f19379884e"
S = "${WORKDIR}/git"

DEPENDS += "openssl"
inherit cmake

FILES:${PN} = "${bindir}/stm32mp-sign-tool"

BBCLASSEXTEND = "native nativesdk"
