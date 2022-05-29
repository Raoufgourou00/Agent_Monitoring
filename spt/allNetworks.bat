@Echo Off
For /F "Skip=1Delims=" %%a In (
    '"WMIC NIC Where (Not NetConnectionStatus Is Null) Get NetConnectionID"'
) Do For /F "Tokens=*" %%b In ("%%a") Do Echo=%%b
Timeout -1
