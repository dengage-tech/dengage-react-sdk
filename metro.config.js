const path = require('path');
const { getDefaultConfig } = require('@react-native/metro-config');

const config = {
  ...getDefaultConfig(__dirname),
  projectRoot: __dirname,
  watchFolders: [path.resolve(__dirname, '..')],
};

module.exports = config; 