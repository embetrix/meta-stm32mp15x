# meta-stm32mp15x

## Summary

**meta-stm32mp15x** is a yocto layer containing BSP support for the [stm32mp15x](https://wiki.st.com/stm32mpu/wiki/STM32MP157x-DKx_-_hardware_description) based devices.

This layer relies on OpenEmbedded/Yocto build system and depends on:

```
[OECORE]
URI: git://git.yoctoproject.org/poky
layers: meta
branch: same dedicated branch as meta-stm32mp15x
```

## List of supported and tested boards:

* [stm32mp157c-dk2](https://www.st.com/en/evaluation-tools/stm32mp157c-dk2.html)
* [stm32mp157f-dk2](https://www.st.com/en/evaluation-tools/stm32mp157f-dk2.html)

## Contributing

If you want to contribute changes, you can send Github pull requests at:
https://github.com/embexus/meta-stm32mp15x/pulls


## Maintainers

 - Ayoub Zaki <info@embetrix.com>
