set param=%1

if %param%=="" or %param%=="b" (
    cls
    packer build packer-ami.json
)

if %param%=="v" (
    cls
    packer validate packer-ami.json
)
