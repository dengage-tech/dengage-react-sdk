import { useEffect, useState } from 'react';
import {
  View,
  Text,
  Switch,
  Button,
  StyleSheet,
  Alert,
  KeyboardAvoidingView,
  Platform,
  ScrollView,
} from 'react-native';
import NoCapTextInput from '../components/NoCapTextInput';
import Dengage from '@dengage-tech/react-native-dengage';

export default function ContactKeyScreen() {
  const [contactKey, setContactKey] = useState('');
  const [permission, setPermission] = useState(false);

  useEffect(() => {
    Dengage.getSubscription?.()
      .then((sub) => {
        setContactKey(sub?.contactKey ?? '');
        setPermission(!!sub?.permission);
      })
      .catch((err) => console.error('Error fetching subscription:', err));
  }, []);

  const saveContactKey = () => {
    Dengage.setContactKey(contactKey.trim());
    Alert.alert('Saved', 'Contact key has been updated.');
  };

  const togglePermission = (value: boolean) => {
    setPermission(value);
    Dengage.setUserPermission(value);
  };

  return (
    <KeyboardAvoidingView
      style={styles.flex}
      behavior={Platform.OS === 'ios' ? 'padding' : undefined}
    >
      <ScrollView contentContainerStyle={styles.container}>
        <NoCapTextInput
          placeholder="Contact Key"
          value={contactKey}
          onChangeText={setContactKey}
        />
        <View style={styles.permissionRow}>
          <Text style={styles.permissionLabel}>User Permission</Text>
          <Switch value={permission} onValueChange={togglePermission} />
        </View>
        <View style={styles.buttonContainer}>
          <Button title="Save" onPress={saveContactKey} />
        </View>
      </ScrollView>
    </KeyboardAvoidingView>
  );
}

const styles = StyleSheet.create({
  flex: { flex: 1 },
  container: { padding: 16 },
  buttonContainer: { marginBottom: 20 },
  permissionRow: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'space-between',
  },
  permissionLabel: { fontSize: 16, color: '#000' },
});
