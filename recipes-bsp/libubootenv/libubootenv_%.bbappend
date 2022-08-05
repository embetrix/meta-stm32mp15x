FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI += "\
	file://fw_env.config \
	"

RDEPENDS:${PN} += "u-boot-default-env"

do_install:append() {

	install -d ${D}${sysconfdir}		
	install -m 0644 ${WORKDIR}/fw_env.config ${D}${sysconfdir}/fw_env.config
}

FILES:${PN} += "${sysconfdir}/fw_env.config"

