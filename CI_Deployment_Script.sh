mkdir ./QAR-1/build-$TRAVIS_BUILD_NUMBER/
gzip ./build/distributions/QAR-1/QAR-1.tar
cp ./build/distributions/QAR-1/QAR-1.tar.gz ./QAR-1/build-$TRAVIS_BUILD_NUMBER/

