import React from 'react';
import { View, Text, StyleSheet } from 'react-native';
import type { RouteProp } from '@react-navigation/native';
import type { RootStackParamList } from '../types';

type PlaceholderScreenProps = {
  route: RouteProp<RootStackParamList, keyof RootStackParamList>;
};

const PlaceholderScreen: React.FC<PlaceholderScreenProps> = ({ route }) => (
  <View style={styles.screenContainer}>
    <Text>{route.name} Screen</Text>
  </View>
);

const styles = StyleSheet.create({
  screenContainer: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
  },
});

export default PlaceholderScreen;