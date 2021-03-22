import * as React from 'react';

import { StyleSheet, View, Text } from 'react-native';
import Dengage from 'react-native-dengage';

export default function App() {
  const [result, setResult] = React.useState<number | undefined>();

  React.useEffect(() => {
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
  }, []);

  return (
    <View style={styles.container}>
      <Text>HasPermission: {result}</Text>
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
