echo off
@REM Get required input from user
set /p ProjectID="Enter Google Project ID "
if not defined ProjectID (
    echo Project ID required
    goto end
)

set /p TopicID="Enter Pub/Sub Topic ID "
if not defined TopicID (
    echo TopicID required
    goto end
)

set /p auth="Enter auth.json path (Must be an absolute path) "
if not defined auth (
    echo auth.json path required
    goto end
)

@REM Set env file
set env=generated.env

@REM Add default inputs to env file
echo PROJECT_ID=%ProjectID%> %env%
echo TOPIC_ID=%TopicID%>> %env%
echo GOOGLE_APPLICATION_CREDENTIALS=/auth.json>> %env%


@REM Options inputs
set /p maxQuestion="Enter maximum possible question (To use default, press enter) "
if defined maxQuestion (
    echo MAX_QUESTION=%maxQuestion%>> %env%
)

set /p waitTime="Enter wait time between messages (To use default, just press enter) "
if defined waitTime (
    echo WAIT_TIME=%waitTime%>> %env%
)

echo on
@REM call build
call docker build -t pub .\Pub

@REM call run
call docker run ^
	--read-only -v %auth%:/auth.json ^
	--env-file .\%env% ^
	--rm -it pub

:end