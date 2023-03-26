require linux-stm32.inc

LINUX_VERSION = "5.15.67"
SRCBRANCH = "v5.15-stm32mp"
SRCREV = "661e4b11da679e4e1f4de088279282f6fbbe528b"

LIC_FILES_CHKSUM = "file://${S}/COPYING;md5=6bc538ed5bd9a7fc9398086aedcd7e46"

SRC_URI += "file://tee.patch"
