#!/bin/sh
#
# see: https://wiki.st.com/stm32mpu/wiki/STM32MP15_backup_registers#Boot_counter_feature
#

BOOTCOUNT_RESET_REG=0x5C00A154

BOOTCOUNT=$(devmem2 $BOOTCOUNT_RESET_REG b)

echo "Bootcount : $BOOTCOUNT"

devmem2 $BOOTCOUNT_RESET_REG b 0x0

# reset u-boot env rollback
if [ ! -z  $(fw_printenv -n rollback) ] ; then
    fw_setenv rollback
fi

if [ ! -z  $(fw_printenv -n ustate) ] ; then
    fw_setenv ustate
fi

echo "Bootcount Reset OK."
