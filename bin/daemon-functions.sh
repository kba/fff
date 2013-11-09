JETTY_RUNNER=dist/jetty-runner-8.1.9.v20130131.jar
PID_FILE="dist/pid/${DAEMON}.pid"

log_warn() {
    echo "!! $1"
}

echo_daemon() {
    echo $DAEMON: $START_CMD
}

stop_command() {
    SIGNAL=$1
    if [ -e $SIGNAL ];then
        signal=-2
    fi
    if [ ! -f $PID_FILE ];then
        log_warn "PID file $PID_FILE doesn't exist"
        return
    fi
    pid=$(cat $PID_FILE)
    if [ -e $pid ];then
        log_warn "PID file $PID_FILE is empty"
    else
        echo Kill $DAEMON with PID $pid
        kill $SIGNAL $pid
        rm $PID_FILE
    fi
}

start_command() {
    command=$*
    if [[ "x$command" = "x" ]];then
        command=$START_CMD
    fi
    $command &> log/${DAEMON}.out & pid=$!
    echo Started $DAEMON as $pid
    echo $command
    echo $pid > $PID_FILE
}
