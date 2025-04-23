import { useEffect, useState } from 'react';
import {
  View,
  Text,
  TextInput,
  Switch,
  Button,
  StyleSheet,
  Alert,
  KeyboardAvoidingView,
  Platform,
  ScrollView,
} from 'react-native';
import Dengage from '@dengage-tech/react-native-dengage';

const ContactKeyScreen = () => {
  const [contactKey, setContactKey] = useState<string>('');
  const [permission, setPermission] = useState<boolean>(false);

  useEffect(() => {
    Dengage.getSubscription?.()
      .then((sub) => {
        if (sub?.contactKey) {
          setContactKey(sub.contactKey);
        }
        if (sub?.permission) {
          setPermission(sub.permission);
        }
      })
      .catch((err) => {
        console.error('Error fetching subscription:', err);
      });
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
        <TextInput
          style={styles.input}
          placeholder="Contact Key"
          value={contactKey}
          onChangeText={setContactKey}
          autoCapitalize="none"
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
};

const styles = StyleSheet.create({
  flex: { flex: 1 },
  container: {
    padding: 16,
    backgroundColor: '#fff',
  },
  input: {
    height: 48,
    borderColor: '#ccc',
    borderWidth: 1,
    borderRadius: 4,
    paddingHorizontal: 12,
    fontSize: 16,
    marginBottom: 12,
  },
  buttonContainer: {
    marginBottom: 20,
  },
  permissionRow: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'space-between',
  },
  permissionLabel: {
    fontSize: 16,
    color: '#000',
  },
});

export default ContactKeyScreen;
