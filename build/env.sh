


RELEASE_DIR="${PROJECT_DIR}/release"
EXTERNAL_DIR="${PROJECT_DIR}/external"
INSTALL_DIR="${PROJECT_DIR}/install"



die() {
  echo "${SCRIPT_BASENAME}: ERROR: $@" 2>&2
  exit 1
}

[ -n "${PROJECT_DIR}" ] || die "environment 'PROJECT_DIR' is not defined"
[ -d "${PROJECT_DIR}" ] || die "directory '${PROJECT_DIR}' not found"