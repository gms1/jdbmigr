


RELEASE_DIR="${PROJECT_DIR}/release"
EXTERNAL_DIR="${PROJECT_DIR}/external"
INSTALL_DIR="${PROJECT_DIR}/install"



msg() {
  echo "$@"
}

info() {
  echo "${SCRIPT_BASENAME}: INFO: $@"
}

warn() {
  echo "${SCRIPT_BASENAME}: WARNING: $@" 2>&1
}

err() {
  echo "${SCRIPT_BASENAME}: ERROR: $@" 2>&1
}


die() {
  err "$@"
  exit 1
}

[ -n "${PROJECT_DIR}" ] || die "environment 'PROJECT_DIR' is not defined"
[ -d "${PROJECT_DIR}" ] || die "directory '${PROJECT_DIR}' not found"
