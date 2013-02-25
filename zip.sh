zip moops.zip $(find moops -path '*/.git' -prune -o -type f -name '*.tar' -prune -o -type f -name '*.mbin' -prune -o -type f -print)
