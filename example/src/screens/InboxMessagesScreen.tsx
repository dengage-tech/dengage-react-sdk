import { useEffect, useState, useCallback } from 'react';
import {
  View,
  Text,
  FlatList,
  StyleSheet,
  TouchableOpacity,
  Alert,
  RefreshControl,
  ActivityIndicator,
} from 'react-native';
import Dengage, { type InboxMessage } from '@dengage-tech/react-native-dengage';

const PAGE_LIMIT = 50;

export default function InboxMessagesScreen() {
  const [messages, setMessages] = useState<InboxMessage[]>([]);
  const [loading, setLoading] = useState(false);
  const [refreshing, setRefreshing] = useState(false);

  const fetchMessages = useCallback(async () => {
    setLoading(true);
    try {
      const msgs: InboxMessage[] = await Dengage.getInboxMessages(
        0,
        PAGE_LIMIT
      );
      setMessages(msgs);
    } catch {
      Alert.alert('Error', 'Failed to fetch messages');
    } finally {
      setLoading(false);
      setRefreshing(false);
    }
  }, []);

  useEffect(() => {
    fetchMessages();
  }, [fetchMessages]);

  const handleDelete = async (id: string) => {
    try {
      await Dengage.deleteInboxMessage?.(id);
      fetchMessages();
    } catch {
      Alert.alert('Error', 'Failed to delete message');
    }
  };

  const handleMarkClicked = async (id: string) => {
    try {
      await Dengage.setInboxMessageAsClicked?.(id);
      fetchMessages();
    } catch {
      Alert.alert('Error', 'Failed to mark as clicked');
    }
  };

  const renderItem = ({ item }: { item: InboxMessage }) => (
    <View style={styles.itemContainer}>
      <View style={{ flex: 1 }}>
        <Text style={styles.title}>{item.title || 'No Title'}</Text>
        <Text style={styles.message}>{item.message || ''}</Text>
        <Text style={styles.date}>{item.receiveDate || ''}</Text>
      </View>
      <View style={styles.actions}>
        <TouchableOpacity
          style={[styles.actionButton, { backgroundColor: '#e74c3c' }]}
          onPress={() =>
            Alert.alert('Delete', 'Delete this message?', [
              { text: 'Cancel', style: 'cancel' },
              {
                text: 'Delete',
                style: 'destructive',
                onPress: () => handleDelete(item.id),
              },
            ])
          }
        >
          <Text style={styles.actionText}>Delete</Text>
        </TouchableOpacity>
        <TouchableOpacity
          style={[styles.actionButton, { backgroundColor: '#2980b9' }]}
          onPress={() => handleMarkClicked(item.id)}
        >
          <Text style={styles.actionText}>Mark as Clicked</Text>
        </TouchableOpacity>
      </View>
    </View>
  );

  if (loading && !refreshing) {
    return (
      <View style={styles.centered}>
        <ActivityIndicator size="large" color="#2980b9" />
      </View>
    );
  }

  return (
    <FlatList
      data={messages}
      keyExtractor={(item) => item.id}
      renderItem={renderItem}
      contentContainerStyle={styles.list}
      refreshControl={
        <RefreshControl
          refreshing={refreshing}
          onRefresh={() => {
            setRefreshing(true);
            fetchMessages();
          }}
        />
      }
      ListEmptyComponent={
        <View style={styles.centered}>
          <Text>No messages found.</Text>
        </View>
      }
    />
  );
}

const styles = StyleSheet.create({
  list: { padding: 16, backgroundColor: '#f2f2f7', flexGrow: 1 },
  itemContainer: {
    backgroundColor: '#fff',
    borderRadius: 12,
    padding: 16,
    marginBottom: 12,
    flexDirection: 'row',
    alignItems: 'flex-start',
    elevation: 1,
    shadowColor: '#000',
    shadowOpacity: 0.04,
    shadowRadius: 2,
    shadowOffset: { width: 0, height: 1 },
  },
  title: { fontWeight: 'bold', fontSize: 15, marginBottom: 4, color: '#222' },
  message: { fontSize: 13, color: '#444', marginBottom: 4 },
  date: { fontSize: 11, color: '#888' },
  actions: { marginLeft: 12, justifyContent: 'space-between' },
  actionButton: {
    paddingVertical: 6,
    paddingHorizontal: 10,
    borderRadius: 6,
    marginBottom: 6,
  },
  actionText: { color: '#fff', fontSize: 12, fontWeight: 'bold' },
  centered: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
    padding: 32,
  },
});
