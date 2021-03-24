import * as React from 'react';

import { StyleSheet, View, Text, Platform } from 'react-native';
import Dengage from 'react-native-dengage';

export default function App() {
  const [result, setResult] = React.useState<string>('checking...');
  const [token, setToken] = React.useState<string>('checking...');
  const [contactKey, setContactKey] = React.useState<string>('checking...');

  React.useEffect(() => {
    if (Platform.OS === 'ios') {
      Dengage.promptForPushNotificationsWitCallback(async (hasPermission) => {
        console.log("hasPermission", hasPermission)
        Dengage.setUserPermission(hasPermission)
        setResult(String(hasPermission))
        if (hasPermission) {
          Dengage.setContactKey("Your-contact-key-here.");
          const token = await Dengage.getToken()
          console.log("tokenIs: ", token)
          setToken(token)
          if (token) {
            Dengage.setToken(token)
            Dengage.setLogStatus(true);
          }
          setContactKey(await Dengage.getContactKey())
        }
      })
    } else {
      Dengage.setContactKey("Your-contact-key-here.");
      const invokeIt = async () => {
        const hasPermissions = await Dengage.getUserPermission()
        setResult(String(hasPermissions))
        const token = await Dengage.getToken()
        setToken(token);
        setContactKey(await Dengage.getContactKey())
      }
      invokeIt()
    }
  }, []);

  return (
    <View style={styles.container}>
      <Text style={styles.heading}>HasPermission</Text>
      <Text>{result}</Text>
      <Text style={styles.heading}>Token</Text>
      <Text>{token}</Text>
      <Text style={styles.heading}>Contact Key</Text>
      <Text>{contactKey}</Text>

    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
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
