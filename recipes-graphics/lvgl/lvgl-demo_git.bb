SUMMARY  = "LVGL demo for frame buffer device"
HOMEPAGE = "https://lvgl.io"
LICENSE  = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=802d3d83ae80ef5f343050bf96cce3a4"

SRC_URI = "gitsm://git@github.com/lvgl/lv_port_linux_frame_buffer;protocol=https;branch=master \
           file://0001-lvgl-set-resolution.patch \
           file://0002-lvgl-set-input-dev.patch  \
           file://lvgl-demo.service              \
		   "
SRCREV = "927d052510a240d0c1d14928fb6d01a2363ebce9"

S = "${WORKDIR}/git"
PV = "+git${SRCPV}"

DEPENDS = "libinput"

do_install() {

	install -d ${D}${bindir}
	install -m 0755 ${S}/build/bin/demo ${D}${bindir}/lvgl-demo
}

FILES:${PN} = "${bindir}"

inherit systemd

SYSTEMD_SERVICE:${PN} = "lvgl-demo.service"
SYSTEMD_PACKAGES = "${PN}"

do_install:append() {

	install -d ${D}${systemd_unitdir}/system
	install -m 0644 ${WORKDIR}/lvgl-demo.service ${D}${systemd_unitdir}/system/
}
