import { View, Text, StyleSheet } from 'react-native';

const NotificationScreen = () => (
  <View style={styles.screenContainer}>
    <Text>ASK NOTIFICATIONS Screen</Text>
  </View>
);

const styles = StyleSheet.create({
  screenContainer: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
  },
});

export default NotificationScreen;