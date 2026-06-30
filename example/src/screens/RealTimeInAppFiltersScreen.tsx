import React from 'react';
import { View, Text, TouchableOpacity, StyleSheet } from 'react-native';
import type { StackNavigationProp } from '@react-navigation/stack';
import type { RootStackParamList } from '../types';

type RealTimeInAppFiltersScreenNavigationProp = StackNavigationProp<
  RootStackParamList,
  'RealTimeInAppFilters'
>;

type RealTimeInAppFiltersScreenProps = {
  navigation: RealTimeInAppFiltersScreenNavigationProp;
};

const RealTimeInAppFiltersScreen: React.FC<RealTimeInAppFiltersScreenProps> = ({
  navigation,
}) => {
  return (
    <View style={styles.container}>
      <TouchableOpacity
        style={styles.button}
        onPress={() => navigation.navigate('EventHistory')}>
        <Text style={styles.buttonText}>Event History</Text>
      </TouchableOpacity>

      <TouchableOpacity
        style={styles.button}
        onPress={() => navigation.navigate('Cart')}>
        <Text style={styles.buttonText}>Cart</Text>
      </TouchableOpacity>
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    padding: 16,
    backgroundColor: '#ffffff',
  },
  button: {
    padding: 16,
    marginVertical: 8,
    backgroundColor: '#d3d3d3',
    alignItems: 'center',
    borderRadius: 8,
  },
  buttonText: {
    fontSize: 16,
    fontWeight: 'bold',
  },
});

export default RealTimeInAppFiltersScreen;

