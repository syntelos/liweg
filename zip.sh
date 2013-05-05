if [ -d liweg ]
then
    rm -f liweg.zip
    zip liweg.zip $(find liweg -path '*/.git' -prune -o -type f -name '*.tar' -prune -o -type f -name '*.libin' -prune -o -type f -print)
    ls -l liweg.zip
else
    1>&2 echo "directory not found, liweg"
fi
