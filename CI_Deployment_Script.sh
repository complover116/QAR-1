PROJNAME=QAR-1

mkdir $PROJNAME
mkdir $PROJNAME/build-$TRAVIS_BUILD_NUMBER/
gzip build/distributions/$PROJNAME.tar
cp build/distributions/$PROJNAME.tar.gz $PROJNAME/build-$TRAVIS_BUILD_NUMBER/

