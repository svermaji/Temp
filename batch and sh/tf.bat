set param=%1

if %param%=="" or %param%=="p" (
    cls
    terraform plan -no-color -refresh=true -out=infra.tfplan
)

if %param%=="a" (
    cls
    terraform apply -refresh=true -no-color -auto-approve "infra.tfplan"
)

if %param%=="d" (
    cls
    terraform destroy -auto-approve -no-color
)
