cmt=$1
if [ -z "$1" ]
then
  cmd='code changes'
fi
echo $cmt

git st
git add .
git cm "$cmt"
git pu
