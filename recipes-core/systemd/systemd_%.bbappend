PACKAGECONFIG:append = " cryptsetup cryptsetup-plugins"

RRECOMMENDS:${PN} += "systemd-crypt systemd-container"
