echo off
@REM Get required input from user
set /p ProjectID="Enter Google Project ID "
if not defined ProjectID (
    echo Project ID required
    goto end
)

set /p subID="Enter Pub/Sub Subscription ID "
if not defined subID (
    echo Subscription ID required
    goto end
)

set /p auth="Enter auth.json path (Must be a absolute path) "
if not defined auth (
    echo auth.json path required
    goto end
)

@REM Set env file
set env=generated.env

@REM Add default inputs to env file
echo PROJECT_ID=%ProjectID%> %env%
echo SUB_ID=%subID%>> %env%
echo GOOGLE_APPLICATION_CREDENTIALS=/auth.json>> %env%


@REM Options inputs
set /p remoteUrl="Enter remote url"
if defined remoteUrl (
    echo REMOTE_URL=%remoteUrl%>> %env%
)

echo on
@REM call build
call docker build -t sub .\Sub

@REM call run
call docker run ^
--read-only -v %auth%:/auth.json ^
--env-file .\%env% ^
--rm -it sub

:end