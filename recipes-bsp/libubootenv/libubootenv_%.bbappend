FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

SRC_URI += "\
	file://fw_env.config \
	"

do_install_append() {

	install -d ${D}${sysconfdir}		
	install -m 0644 ${WORKDIR}/fw_env.config ${D}${sysconfdir}/fw_env.config
}

FILES_${PN} += "${sysconfdir}/fw_env.config"

SRCREV = "ba7564f5006d09bec51058cf4f5ac90d4dc18b3c"
PV = "0.3.2"