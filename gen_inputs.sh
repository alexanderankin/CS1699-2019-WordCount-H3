#!/usr/bin/env bash

# Exit with error message
die() { echo "$*" 1>&2 ; exit 1; }

# POSIX Check if the command exists
command_exists () {
    command -v $1 >/dev/null;
}

# Prevent program from running without prerequisites
check_command () {
	if command_exists $1
	then
		echo "found $1"
	else
		die missing $1
	fi
}

# prerequisites
check_command python
check_command pip
check_command virtualenv

# If necessary, make environment
if ! [[ -d env ]]; then
	virtualenv env
	source env/bin/activate
	pip install lorem
fi

# enter environment
source env/bin/activate

# generate scripts
python << EOF
import lorem
files = ['input' + str(i) + '.txt' for i in [1,2,3]]
for file in files:
	with open(file, "w") as f:
		for i in range(30 * 1000):
			f.write(lorem.paragraph())
			f.write('\n')
EOF
