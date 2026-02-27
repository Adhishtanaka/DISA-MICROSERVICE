import { useRef, useState } from 'react'
import { assessmentAPI } from '../lib/api'
import { Upload, Image as ImageIcon, Loader } from 'lucide-react'

interface PhotoUploadProps {
  assessmentId: number
  photos: string[]
  onPhotoAdded: (newPhoto: string) => void
}

export function PhotoUpload({ assessmentId, photos, onPhotoAdded }: PhotoUploadProps) {
  const inputRef = useRef<HTMLInputElement>(null)
  const [uploading, setUploading] = useState(false)
  const [error, setError] = useState<string | null>(null)
  const [dragActive, setDragActive] = useState(false)

  const handleDrag = (e: React.DragEvent) => {
    e.preventDefault()
    e.stopPropagation()
    if (e.type === 'dragenter' || e.type === 'dragover') {
      setDragActive(true)
    } else if (e.type === 'dragleave') {
      setDragActive(false)
    }
  }

  const handleDrop = async (e: React.DragEvent) => {
    e.preventDefault()
    e.stopPropagation()
    setDragActive(false)

    const files = e.dataTransfer.files
    if (files && files[0]) {
      await uploadFile(files[0])
    }
  }

  const uploadFile = async (file: File) => {
    if (!file.type.startsWith('image/')) {
      setError('Please upload an image file')
      return
    }

    if (file.size > 10 * 1024 * 1024) {
      setError('File size must be less than 10MB')
      return
    }

    setUploading(true)
    setError(null)

    try {
      const formData = new FormData()
      formData.append('file', file)
      
      const response = await assessmentAPI.uploadPhoto(assessmentId, formData)
      onPhotoAdded(response.data.id)
    } catch (err: any) {
      setError(err.response?.data?.message || 'Failed to upload photo')
    } finally {
      setUploading(false)
      if (inputRef.current) {
        inputRef.current.value = ''
      }
    }
  }

  const handleFileSelect = async (e: React.ChangeEvent<HTMLInputElement>) => {
    if (e.target.files?.[0]) {
      await uploadFile(e.target.files[0])
    }
  }

  return (
    <div className="space-y-4">
      <div
        onDragEnter={handleDrag}
        onDragLeave={handleDrag}
        onDragOver={handleDrag}
        onDrop={handleDrop}
        className={`relative border-2 border-dashed rounded-lg p-8 text-center transition-colors ${
          dragActive ? 'border-blue-500 bg-blue-50' : 'border-gray-300 bg-gray-50'
        } ${uploading ? 'opacity-50' : ''}`}
      >
        <input
          ref={inputRef}
          type="file"
          accept="image/*"
          onChange={handleFileSelect}
          className="hidden"
          disabled={uploading}
        />

        <div className="flex flex-col items-center gap-3">
          <Upload className={`w-8 h-8 ${dragActive ? 'text-blue-500' : 'text-gray-400'}`} />
          <div>
            <p className="text-sm font-medium text-gray-900">
              {dragActive ? 'Drop your image here' : 'Drag and drop your image here'}
            </p>
            <p className="text-xs text-gray-500 mt-1">or</p>
          </div>
          <button
            type="button"
            onClick={() => inputRef.current?.click()}
            disabled={uploading}
            className="px-4 py-2 bg-blue-600 text-white rounded-lg text-sm font-medium hover:bg-blue-700 transition-colors disabled:opacity-50"
          >
            {uploading ? (
              <>
                <Loader className="w-4 h-4 inline mr-2 animate-spin" />
                Uploading...
              </>
            ) : (
              'Select File'
            )}
          </button>
          <p className="text-xs text-gray-500">PNG, JPG, GIF up to 10MB</p>
        </div>
      </div>

      {error && (
        <div className="bg-red-50 border border-red-200 rounded-lg p-3 text-red-700 text-sm">
          {error}
        </div>
      )}

      {photos && photos.length > 0 && (
        <div>
          <h4 className="text-sm font-medium text-gray-900 mb-3">Uploaded Photos</h4>
          <div className="grid grid-cols-2 sm:grid-cols-3 gap-4">
            {photos.map((photo, index) => (
              <div key={index} className="relative group">
                <div className="bg-gray-100 rounded-lg aspect-square overflow-hidden">
                  <img
                    src={photo}
                    alt={`Assessment photo ${index + 1}`}
                    className="w-full h-full object-cover"
                  />
                </div>
                <div className="absolute inset-0 bg-black bg-opacity-0 group-hover:bg-opacity-40 rounded-lg transition-all flex items-center justify-center opacity-0 group-hover:opacity-100">
                  <ImageIcon className="w-6 h-6 text-white" />
                </div>
              </div>
            ))}
          </div>
        </div>
      )}
    </div>
  )
}
