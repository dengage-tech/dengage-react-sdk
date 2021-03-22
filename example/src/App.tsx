import * as React from 'react';

import { StyleSheet, View, Text } from 'react-native';
import Dengage from 'react-native-dengage';

export default function App() {
  const [result, setResult] = React.useState<number | undefined>();

  React.useEffect(() => {
    Dengage.multiply(3, 7).then(setResult);
    // Dengage.setIntegerationKey("KgSgzg1Ac14Jf_p_l_c6V_s_l_7EqnhH1UinXdp_s_l_N7E3ZU4afMfZJcDQPIGm8Y4KX57kE018fi3IfQOhSZMoPA007Ylaod9n7l_s_l_ic9S6Mxk8CnP6o36CijU7hu4BQWeyN_p_l_g_s_l_hk4y")
     Dengage.promptForPushNotificationsWitCallback((hasPermission) => {
       console.log("hasPermission", hasPermission)
     })
  }, []);

  return (
    <View style={styles.container}>
      <Text>Result: {result}</Text>
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
