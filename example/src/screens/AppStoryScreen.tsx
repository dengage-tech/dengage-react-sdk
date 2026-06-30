import { useState } from 'react';
import { Button, ScrollView, StyleSheet, Text, View } from 'react-native';
import NoCapTextInput from '../components/NoCapTextInput';
import { StoriesListView } from '@dengage-tech/react-native-dengage';

export default function AppStoryScreen() {
  const [propertyId, setPropertyId] = useState('1');
  const [screenName, setScreenName] = useState('appstory');
  const [storyBackgroundColor, setStoryBackgroundColor] = useState('#ffffff');
  const [containerColor, setContainerColor] = useState('#ffffff');
  const [showStory, setShowStory] = useState(false);
  const [nativeReportsHidden, setNativeReportsHidden] = useState(false);

  const hideIfNotFound = true;
  const collapseSlot = hideIfNotFound && nativeReportsHidden;

  return (
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
      <Button
        title="Change Story Background Color"
        onPress={() => setContainerColor(storyBackgroundColor)}
      />
      <Button
        title="Refresh Story"
        onPress={() => {
          setShowStory(true);
          setNativeReportsHidden(false);
        }}
      />
      {showStory && !collapseSlot && (
        <View style={styles.storySlot}>
          <StoriesListView
            storyPropertyId={propertyId.trim()}
            screenName={screenName.trim()}
            customParams={{}}
            hideIfNotFound={hideIfNotFound}
            onStoryVisibilityChanged={(e) =>
              setNativeReportsHidden(e.nativeEvent.isHidden)
            }
            style={[styles.story, { backgroundColor: containerColor }]}
          />
        </View>
      )}
      {showStory && (
        <Text style={styles.sizeTestHint}>
          Testing size: this line should sit directly under the button when the
          story slot collapses (hidden / not found). If you still see a big gap
          above this text, collapse did not reclaim height.
        </Text>
      )}
    </ScrollView>
  );
}

const styles = StyleSheet.create({
  container: { padding: 16 },
  storySlot: {
    marginTop: 16,
    alignSelf: 'stretch',
  },
  story: {
    height: 300,
  },
  sizeTestHint: {
    marginTop: 12,
    fontSize: 13,
    lineHeight: 18,
    color: '#444',
  },
});
