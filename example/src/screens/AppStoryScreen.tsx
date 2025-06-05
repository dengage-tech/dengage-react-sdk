import { useState } from 'react';
import {
  SafeAreaView,
  View,
  Button,
  StyleSheet,
  KeyboardAvoidingView,
  Platform,
  ScrollView,
} from 'react-native';
import NoCapTextInput from '../components/NoCapTextInput';
import { StoriesListView } from '@dengage-tech/react-native-dengage';

export default function AppStoryScreen() {
  const [propertyId, setPropertyId] = useState('1');
  const [screenName, setScreenName] = useState('appstory');
  const [storyBackgroundColor, setStoryBackgroundColor] = useState('#ffffff');
  const [containerColor, setContainerColor] = useState('#ffffff');
  const [storyConfig, setStoryConfig] = useState<{
    propertyId: string;
    screenName: string;
    customParams: Record<string, string>;
  } | null>(null);

  const changeStoryBackgroundColor = () =>
    setContainerColor(storyBackgroundColor);
  const refreshStory = () =>
    setStoryConfig({
      propertyId: propertyId.trim(),
      screenName: screenName.trim(),
      customParams: {},
    });

  return (
    <SafeAreaView style={styles.flex}>
      <KeyboardAvoidingView
        style={styles.flex}
        behavior={Platform.OS === 'ios' ? 'padding' : undefined}
      >
        <ScrollView contentContainerStyle={styles.container}>
          <NoCapTextInput
            placeholder="Property Id"
            value={propertyId}
            onChangeText={setPropertyId}
          />
          <NoCapTextInput
            placeholder="Screen Name"
            value={screenName}
            onChangeText={setScreenName}
          />
          <NoCapTextInput
            placeholder="Story Background Color"
            value={storyBackgroundColor}
            onChangeText={setStoryBackgroundColor}
          />
          <View style={styles.buttonMargin}>
            <Button
              title="Change Story Background Color"
              onPress={changeStoryBackgroundColor}
            />
          </View>
          <View style={styles.buttonMargin}>
            <Button title="Refresh Story" onPress={refreshStory} />
          </View>
          <View
            style={[styles.storyContainer, { backgroundColor: containerColor }]}
          >
            {storyConfig && (
              <StoriesListView
                storyPropertyId={storyConfig.propertyId}
                screenName={storyConfig.screenName}
                customParams={storyConfig.customParams}
                style={{ backgroundColor: containerColor }}
              />
            )}
          </View>
        </ScrollView>
      </KeyboardAvoidingView>
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  flex: { flex: 1 },
  container: { padding: 16, backgroundColor: '#f0f0f0' },
  buttonMargin: { marginTop: 12 },
  storyContainer: { width: '100%', marginTop: 16 },
});
