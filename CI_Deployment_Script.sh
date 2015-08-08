PROJNAME=QAR-1

mkdir $PROJNAME
mkdir $PROJNAME/build-$TRAVIS_BUILD_NUMBER/
#GZ the tarball and copy it to the output folder
gzip build/distributions/$PROJNAME.tar
cp build/distributions/$PROJNAME.tar.gz $PROJNAME/build-$TRAVIS_BUILD_NUMBER/
#Remove everything so travis does not get confused
rm -f *
rm -rf build
rm -rf gradle

