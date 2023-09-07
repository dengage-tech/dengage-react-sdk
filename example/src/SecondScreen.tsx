import * as React from 'react';

import { StyleSheet, Text, View, Button } from 'react-native';
import { useFocusEffect, useNavigation } from '@react-navigation/native';
import Dengage from '@dengage-tech/react-native-dengage';

export default function SecondScreen() {
  const navigation = useNavigation()
  useFocusEffect(
    React.useCallback(() => {
      Dengage.setNavigation();
      //Dengage.showRealTimeInApp("kj",{});
    }, [])
  );
  return (
    <View style={styles.container}>
      <Text style={styles.heading}>-:Second Screen:-</Text>
      <Button title={"go back"} onPress={() => {
        navigation.goBack()
      }}/>
      <Text>Note: On goBack, setNavigation is called.</Text>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'space-evenly',
    alignItems: 'center',
    padding: 20,
  },
  box: {
    width: 60,
    height: 60,
    marginVertical: 20,
  },
  heading: {
    fontWeight: 'bold',
    marginTop: 20
  }
});
