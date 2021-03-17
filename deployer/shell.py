import sys
from subprocess import call

OK = 0


def run_shell_command(command, input_stream=sys.stdin, output_stream=sys.stdout, exit_on_error=True):
    ret = call(command, stdin=input_stream, stdout=output_stream)
    if ret != OK and exit_on_error:
        print("Errors found during command execution: " + ' '.join(command))
        exit()
