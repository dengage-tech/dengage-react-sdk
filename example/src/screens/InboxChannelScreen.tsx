import { useEffect, useState, useCallback } from 'react';
import {
  View,
  Text,
  Image,
  FlatList,
  StyleSheet,
  TouchableOpacity,
  Alert,
  Linking,
  Platform,
  RefreshControl,
  ActivityIndicator,
} from 'react-native';
import Dengage, {
  type InboxChannelMessage,
  type InboxChannelEventType,
  type InboxChannelCtaButton,
} from '@dengage-tech/react-native-dengage';

const PAGE_LIMIT = 20;

const toEvent = (type: InboxChannelEventType, m: InboxChannelMessage) => ({
  eventType: type,
  messageId: m.id,
  messageDetails: m.data.messageDetails,
});

const deeplinkOf = (cta?: InboxChannelCtaButton): string | undefined => {
  const native = Platform.OS === 'ios' ? cta?.iosDeeplink : cta?.androidDeeplink;
  return native || cta?.webUrl || undefined;
};

export default function InboxChannelScreen() {
  const [messages, setMessages] = useState<InboxChannelMessage[]>([]);
  const [loading, setLoading] = useState(false);
  const [refreshing, setRefreshing] = useState(false);

  const fetchMessages = useCallback(async () => {
    setLoading(true);
    try {
      const msgs = await Dengage.getInboxChannelMessages(PAGE_LIMIT);
      setMessages(msgs);
      // Report an impression (IM) for every message currently shown, in bulk.
      if (msgs.length > 0) {
        await Dengage.sendInboxChannelEvents(msgs.map((m) => toEvent('IM', m)));
      }
    } catch (e) {
      Alert.alert('Error', `Failed to fetch inbox channel messages: ${e}`);
    } finally {
      setLoading(false);
      setRefreshing(false);
    }
  }, []);

  useEffect(() => {
    fetchMessages();
  }, [fetchMessages]);

  const markRead = (id: string) =>
    setMessages((prev) =>
      prev.map((m) => (m.id === id ? { ...m, isRead: true } : m))
    );

  const openLink = async (link?: string) => {
    if (!link) {
      Alert.alert('No deeplink for this message');
      return;
    }
    try {
      await Linking.openURL(link);
    } catch {
      Alert.alert('Cannot open', link);
    }
  };

  const handleOpen = async (m: InboxChannelMessage) => {
    markRead(m.id);
    try {
      await Dengage.sendInboxChannelEvents([toEvent('OP', m)]);
    } catch (e) {
      Alert.alert('Error', `Failed to send open event: ${e}`);
    }
  };

  const handleClick = async (m: InboxChannelMessage, cta?: InboxChannelCtaButton) => {
    markRead(m.id);
    try {
      await Dengage.sendInboxChannelEvents([toEvent('CL', m)]);
    } catch (e) {
      Alert.alert('Error', `Failed to send click event: ${e}`);
    }
    openLink(deeplinkOf(cta ?? m.data.ctaButtons?.[0]));
  };

  const handleDelete = async (m: InboxChannelMessage) => {
    // Reflect the delete on the UI immediately; the server processes the event async.
    setMessages((prev) => prev.filter((x) => x.id !== m.id));
    try {
      await Dengage.sendInboxChannelEvents([toEvent('DT', m)]);
    } catch (e) {
      Alert.alert('Error', `Failed to send delete event: ${e}`);
    }
  };

  const renderItem = ({ item }: { item: InboxChannelMessage }) => (
    <TouchableOpacity
      activeOpacity={0.8}
      style={[styles.card, !item.isRead && styles.unread]}
      onPress={() => handleClick(item)}
    >
      {item.data.imageUrl ? (
        <Image source={{ uri: item.data.imageUrl }} style={styles.image} />
      ) : null}
      <View style={styles.row}>
        <Text style={styles.title}>
          {item.data.isPinned ? '📌 ' : ''}
          {item.data.title || 'No Title'}
        </Text>
        <Text style={item.isRead ? styles.read : styles.unreadLabel}>
          {item.isRead ? 'Read' : 'Unread'}
        </Text>
      </View>
      <Text style={styles.message}>{item.data.message || ''}</Text>
      <Text style={styles.date}>{item.data.receiveDate || ''}</Text>

      <View style={styles.buttons}>
        {(item.data.ctaButtons ?? []).map((cta, i) => (
          <TouchableOpacity
            key={cta.buttonId ?? String(i)}
            style={[styles.button, { backgroundColor: '#27ae60' }]}
            onPress={() => handleClick(item, cta)}
          >
            <Text style={styles.buttonText}>{cta.label || 'Open'}</Text>
          </TouchableOpacity>
        ))}
        <TouchableOpacity
          style={[styles.button, { backgroundColor: '#2980b9' }]}
          onPress={() => handleOpen(item)}
        >
          <Text style={styles.buttonText}>Mark Opened</Text>
        </TouchableOpacity>
        <TouchableOpacity
          style={[styles.button, { backgroundColor: '#e74c3c' }]}
          onPress={() =>
            Alert.alert('Delete', 'Delete this message?', [
              { text: 'Cancel', style: 'cancel' },
              { text: 'Delete', style: 'destructive', onPress: () => handleDelete(item) },
            ])
          }
        >
          <Text style={styles.buttonText}>Delete</Text>
        </TouchableOpacity>
      </View>
    </TouchableOpacity>
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
          <Text>No inbox channel messages.</Text>
        </View>
      }
    />
  );
}

const styles = StyleSheet.create({
  list: { padding: 16, backgroundColor: '#f2f2f7', flexGrow: 1 },
  card: {
    backgroundColor: '#fff',
    borderRadius: 12,
    padding: 16,
    marginBottom: 12,
    elevation: 1,
    shadowColor: '#000',
    shadowOpacity: 0.04,
    shadowRadius: 2,
    shadowOffset: { width: 0, height: 1 },
  },
  unread: { borderLeftWidth: 4, borderLeftColor: '#2980b9' },
  image: { width: '100%', height: 140, borderRadius: 8, marginBottom: 8 },
  row: { flexDirection: 'row', justifyContent: 'space-between' },
  title: { flex: 1, fontWeight: 'bold', fontSize: 15, color: '#222' },
  message: { fontSize: 13, color: '#444', marginTop: 4 },
  date: { fontSize: 11, color: '#888', marginTop: 4 },
  read: { fontSize: 11, color: '#888' },
  unreadLabel: { fontSize: 11, color: '#2980b9', fontWeight: 'bold' },
  buttons: { flexDirection: 'row', flexWrap: 'wrap', marginTop: 10 },
  button: {
    paddingVertical: 6,
    paddingHorizontal: 10,
    borderRadius: 6,
    marginRight: 8,
    marginBottom: 6,
  },
  buttonText: { color: '#fff', fontSize: 12, fontWeight: 'bold' },
  centered: { flex: 1, alignItems: 'center', justifyContent: 'center', padding: 32 },
});
