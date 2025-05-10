import {
  SafeAreaView,
  View,
  Button,
  StyleSheet,
  KeyboardAvoidingView,
  Platform,
  ScrollView,
} from 'react-native';
import Dengage from '@dengage-tech/react-native-dengage';

export default function GeofenceScreen() {


  const requestLocationPermissions = () => {
    Dengage.requestLocationPermissions();
  };

  const stopGeofence = () => {
    Dengage.stopGeofence();
  };

  return (
    <SafeAreaView style={styles.flex}>
      <KeyboardAvoidingView
        style={styles.flex}
        behavior={Platform.OS === 'ios' ? 'padding' : undefined}
      >
        <ScrollView contentContainerStyle={styles.container}>
          <View style={styles.buttonMargin}>
            <Button
              title="Request Location Always Authorization"
              onPress={requestLocationPermissions}
            />
          </View>

          <View style={styles.buttonMargin}>
            <Button title="Stop Geofence" onPress={stopGeofence} />
          </View>
        </ScrollView>
      </KeyboardAvoidingView>
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  flex: { flex: 1 },
  container: {
    padding: 16,
    backgroundColor: '#f0f0f0',
  },
  buttonMargin: {
    marginTop: 12,
  },
});
