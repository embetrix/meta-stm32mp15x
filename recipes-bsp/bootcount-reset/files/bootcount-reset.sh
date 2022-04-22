#!/bin/sh

BOOTCOUNT_RESET_FRAM_REG=0x5C00A154

FAILED_UNITS=$(systemctl list-units | grep -i failed)
BOOTCOUNT=$(devmem2 $BOOTCOUNT_RESET_FRAM_REG b)

echo "Bootcount : $BOOTCOUNT"

# Reset bootcount only if there are no failed units
if [ -z "$FAILED_UNITS" ]; then
    devmem2 $BOOTCOUNT_RESET_FRAM_REG b 0x0
fi 

