import { useCallback, useEffect, useState } from 'react';
import { View, Text, StyleSheet, TouchableOpacity } from 'react-native';
import Dengage from '@dengage-tech/react-native-dengage';

const STATUSES = ['always', 'appinuse', 'none'];

export default function LocationPermissionScreen() {
  const [current, setCurrent] = useState<string>('-');

  const refresh = useCallback(async () => {
    try {
      const sub = await Dengage.getSubscription();
      setCurrent(sub?.locationPermission || '-');
    } catch (e) {
      console.error('Error fetching subscription:', e);
    }
  }, []);

  useEffect(() => {
    refresh();
  }, [refresh]);

  const apply = (status: string) => {
    Dengage.setLocationPermission(status);
    // Native updates its subscription asynchronously, give it a moment.
    setTimeout(refresh, 800);
  };

  return (
    <View style={styles.container}>
      <Text style={styles.label}>Current location permission</Text>
      <Text style={styles.value}>{current}</Text>
      {STATUSES.map((status) => (
        <TouchableOpacity
          key={status}
          style={styles.button}
          onPress={() => apply(status)}
        >
          <Text style={styles.buttonText}>SET "{status}"</Text>
        </TouchableOpacity>
      ))}
    </View>
  );
}

const styles = StyleSheet.create({
  container: { flex: 1, padding: 16, backgroundColor: '#ffffff' },
  label: { fontSize: 14, color: '#666' },
  value: { fontSize: 20, fontWeight: 'bold', color: '#000', marginBottom: 24 },
  button: {
    backgroundColor: '#2980b9',
    padding: 14,
    borderRadius: 8,
    marginBottom: 12,
    alignItems: 'center',
  },
  buttonText: { color: '#fff', fontWeight: 'bold' },
});
