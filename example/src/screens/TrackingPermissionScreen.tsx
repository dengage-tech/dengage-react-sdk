import { useEffect, useState } from 'react';
import { View, Text, Switch, StyleSheet, ActivityIndicator } from 'react-native';
import Dengage from '@dengage-tech/react-native-dengage';

export default function TrackingPermissionScreen() {
  const [trackingPermission, setTrackingPermission] = useState(false);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    Dengage.getTrackingPermission()
      .then((permission) => setTrackingPermission(permission))
      .catch((err) => console.error('Error fetching tracking permission:', err))
      .finally(() => setLoading(false));
  }, []);

  const toggleTrackingPermission = (value: boolean) => {
    setTrackingPermission(value);
    Dengage.setTrackingPermission(value);
    console.log('trackingPermission:', value);
  };

  if (loading) {
    return (
      <View style={styles.centered}>
        <ActivityIndicator size="large" />
      </View>
    );
  }

  return (
    <View style={styles.container}>
      <View style={styles.permissionRow}>
        <Text style={styles.permissionLabel}>Tracking Permission</Text>
        <Switch
          value={trackingPermission}
          onValueChange={toggleTrackingPermission}
        />
      </View>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    padding: 16,
    backgroundColor: '#ffffff',
  },
  centered: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#ffffff',
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
