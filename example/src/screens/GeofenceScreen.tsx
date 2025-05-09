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


  const changeStoryBackgroundColor = () => {
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
              onPress={changeStoryBackgroundColor}
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
  input: {
    height: 48,
    borderColor: '#ccc',
    borderWidth: 1,
    borderRadius: 4,
    paddingHorizontal: 12,
    fontSize: 16,
    marginBottom: 12,
    backgroundColor: '#fff',
  },
  buttonMargin: {
    marginTop: 12,
  },
  storyContainer: {
    width: '100%',
    marginTop: 16,
  },
});
