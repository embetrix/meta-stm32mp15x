DESCRIPTION = "Bootcount Reset"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/GPL-2.0-or-later;md5=fed54355545ffd980b814dab4a3b312c"

RDEPENDS:${PN} = "devmem2"

REQUIRED_DISTRO_FEATURES = "systemd"

inherit allarch systemd

SRC_URI = "file://bootcount-reset.timer \
           file://bootcount-reset.service \
           file://bootcount-reset.sh \
          "	

do_install () {

	install -d ${D}${bindir}
	install -m 0755 ${WORKDIR}/bootcount-reset.sh ${D}${bindir}/bootcount-reset
	
	install -d ${D}${systemd_unitdir}/system
	install -m 0644 ${WORKDIR}/bootcount-reset.timer ${D}${systemd_unitdir}/system/
	install -m 0644 ${WORKDIR}/bootcount-reset.service ${D}${systemd_unitdir}/system/
}

SYSTEMD_PACKAGES = "${PN}"
SYSTEMD_SERVICE:${PN} = "bootcount-reset.timer"

FILES:${PN} = "${bindir} ${systemd_unitdir}/system/"

