$ARTIFACT_PATTERN = "target/jwt_authentication_service*.jar"
if (Test-Path $ARTIFACT_PATTERN)
{
    Write-Host "Artifact already exists. Skipping Maven package."
}
else
{
    Write-Host "Artifact does not exist. Running Maven package."
    mvn package -Dnative
}
docker build -f src/main/docker/Dockerfile -t app/jwt-auth .
