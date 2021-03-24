import * as React from 'react';

import { StyleSheet, View, Text, Platform } from 'react-native';
import Dengage from 'react-native-dengage';

export default function App() {
  const [result, setResult] = React.useState<number | undefined>('checking...');

  React.useEffect(() => {
    if (Platform.OS === 'ios') {
      Dengage.promptForPushNotificationsWitCallback(async (hasPermission) => {
        console.log("hasPermission", hasPermission)
        Dengage.setUserPermission(hasPermission)
        setResult(String(hasPermission))
        if (hasPermission) {
          const token = await Dengage.getToken()
          console.log("tokenIs: ", token)
          if (token) {
            Dengage.setToken(token)
            Dengage.setLogStatus(true);
          }
        }
      })
    } else {
      Dengage.setContactKey("Your-contact-key-here.");
      const invokeIt = async () => {
        const token = await Dengage.getToken()
        setResult(token);
      }
      invokeIt()
    }
  }, []);

  return (
    <View style={styles.container}>
      <Text>{Platform.OS === 'ios' ? 'HasPermission: ' : 'Token: '}{result}</Text>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
  },
  box: {
    width: 60,
    height: 60,
    marginVertical: 20,
  },
});
