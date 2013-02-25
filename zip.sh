if [ -d moops ]
then
    rm -f moops.zip
    zip moops.zip $(find moops -path '*/.git' -prune -o -type f -name '*.tar' -prune -o -type f -name '*.mbin' -prune -o -type f -print)
    ls -l moops.zip
else
    1>&2 echo "directory not found, moops"
fi
