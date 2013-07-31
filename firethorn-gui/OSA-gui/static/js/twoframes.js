function FrameChange (URI, Framename) {
  parent[Framename].location.href = URI;
}

function TwoFramesBack (Framename1, Framename2) {
  Frame2 = parent[Framename2];
  parent[Framename1].history.back();
  if (!window.opera)
    window.setTimeout("Frame2.history.back()", 100);
}

function TwoFramesForward (Framename1, Framename2) {
  Frame2 = parent[Framename2];
  parent[Framename1].history.forward();
  if (!window.opera)
    window.setTimeout("Frame2.history.forward()", 100);
}
